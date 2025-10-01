export interface UploadImageRequest {
  file?: File;
}
export interface UploadImageResponse {
  secure_url: string;
}

export const uploadImageUseCase = async (
  request: UploadImageRequest
): Promise<UploadImageResponse> => {
  if (!request.file) return { secure_url: "" };

  const config = useRuntimeConfig();
  const url = `https://api.cloudinary.com/v1_1/${config.public.cloudinaryName}/upload`;

  const formData = new FormData();
  formData.append("file", request.file);
  formData.append("upload_preset", "astikos-vizo");

  const response = await fetch(url, {
    method: "POST",
    body: formData,
  });

  if (!response.ok) {
    throw new Error("Failed to upload image");
  }

  const data = await response.json();
  if (data.error) throw new Error("Failed to upload image: " + data.error);

  return data;
};
