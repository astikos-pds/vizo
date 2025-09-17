FROM python:3.12-slim

WORKDIR /app

COPY pyproject.toml poetry.lock* /app/

RUN pip install poetry && poetry install --no-root

COPY . /app

EXPOSE 5000

CMD ["poetry", "run", "uvicorn", "src.analyser.main:app", "--host", "0.0.0.0", "--port", "5000"]
