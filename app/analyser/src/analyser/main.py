from io import BytesIO
from typing import List
from fastapi import FastAPI
from pydantic import BaseModel
from PIL import Image
import requests
import torch
from transformers import CLIPModel, CLIPProcessor
from analyser.mapping import problem_type_map

app = FastAPI()

model = CLIPModel.from_pretrained("openai/clip-vit-base-patch32")
processor = CLIPProcessor.from_pretrained("openai/clip-vit-base-patch32")

class ClassifyRequest(BaseModel):
    description: str
    images_urls: List[str]
    categories: List[str]

@app.post("/classify")
async def classify(request: ClassifyRequest):
    labels = [problem_type_map[categorie] for categorie in request.categories]

    images = []
    for url in request.images_urls:
        try:
            payload = requests.get(url, timeout=5)
            image = Image.open(BytesIO(payload.content)).convert("RGB")
            images.append(image)
        except Exception:
            continue

    text = [request.description] + labels

    if len(images) > 0:
        inputs = processor(
            text=text,
            images=images,
            return_tensors="pt",
            padding=True
        )

        outputs = model(**inputs)
    
        text_embeds = outputs.text_embeds / outputs.text_embeds.norm(p=2, dim=-1, keepdim=True)
        image_embeds = outputs.image_embeds / outputs.image_embeds.norm(p=2, dim=-1, keepdim=True)
    else:
        inputs = processor(
            text=text,
            return_tensors="pt",
            padding=True
        )

        text_features = model.get_text_features(**inputs)
        text_embeds = text_features / text_features.norm(p=2, dim=-1, keepdim=True)
        image_embeds = None

    desc_embed = text_embeds[0:1]
    category_embeds = text_embeds[1:]

    if image_embeds is not None:
        combined_embed = torch.cat([desc_embed, image_embeds], dim=0).mean(dim=0, keepdim=True)
    else:
        combined_embed = desc_embed

    sims = (combined_embed @ category_embeds.T).squeeze(0)
    best_idx = int(torch.argmax(sims))
    predicted_enum = request.categories[best_idx]
    confidence = sims[best_idx].item()

    if image_embeds is not None:
        sims_text_img = (desc_embed @ image_embeds.T).squeeze(0)
        per_image_scores = sims_text_img.tolist()
        text_image_consistency = float(sum(per_image_scores) / len(per_image_scores))
    else:
        per_image_scores = []
        text_image_consistency = 1.0

    return {
        "predicted_type": predicted_enum,
        "confidence": round(confidence, 2),
        "text_image_consistency": round(text_image_consistency, 2),
        "per_image_scores": [round(score, 2) for score in per_image_scores] 
    }