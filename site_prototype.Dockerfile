FROM python:3.12-slim

ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

WORKDIR /app

RUN apt-get update \
    && apt-get install -y libpq-dev netcat-openbsd \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

RUN pip install --upgrade pip
RUN pip install poetry

COPY site_prototype/pyproject.toml site_prototype/poetry.lock /app/

RUN poetry install

COPY site_prototype /app/

RUN chmod +x /app/entrypoint.sh
