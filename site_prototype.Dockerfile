FROM python:3.12-slim

ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

WORKDIR /app

RUN apt-get update && \
    apt-get install -y --no-install-recommends libpq-dev netcat-openbsd && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

RUN pip install --upgrade pip && \
    pip install poetry

COPY site_prototype/pyproject.toml site_prototype/poetry.lock /app/

RUN poetry install --no-dev

COPY site_prototype/manage.py /app/manage.py
COPY site_prototype/shop /app/shop
COPY site_prototype/carts /app/carts
COPY site_prototype/products /app/products
COPY site_prototype/users /app/users
COPY site_prototype/templates /app/templates
COPY site_prototype/static /app/static
COPY site_prototype/media /app/media
