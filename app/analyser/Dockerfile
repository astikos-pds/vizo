FROM python:3.12-slim AS builder

WORKDIR /app

RUN apt-get update && apt-get install -y build-essential && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

COPY pyproject.toml poetry.lock /app/

RUN pip install --upgrade pip && pip install poetry && \
    poetry config virtualenvs.create false && \
    poetry install --no-root 

COPY . /app

FROM python:3.12-slim

WORKDIR /app

COPY --from=builder /usr/local/lib/python3.12/site-packages /usr/local/lib/python3.12/site-packages
COPY --from=builder /app /app

EXPOSE 5000

CMD ["python", "-m", "uvicorn", "src.analyser.main:app", "--host", "0.0.0.0", "--port", "5000"]
