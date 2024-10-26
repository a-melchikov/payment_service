#!/bin/bash

echo "Waiting for postgres..."
for i in {30..0}; do
  if nc -z db 5432; then
    break
  fi
  echo "Postgres is unavailable - sleeping"
  sleep 1
done

if [ "$i" == 0 ]; then
  echo "PostgreSQL did not start in time"
  exit 1
fi

if poetry run python manage.py migrate; then
  echo "Migrations applied successfully"
else
  echo "Migration failed"
  exit 1
fi

if poetry run python manage.py shell -c "from django.contrib.auth import get_user_model; User = get_user_model(); User.objects.filter(username='admin').exists() or User.objects.create_superuser('admin', 'admin@example.com', 'admin')"; then
  echo "Superuser created or already exists"
else
  echo "Superuser creation failed"
  exit 1
fi

if poetry run python manage.py populate_db; then
  echo "Database populated successfully"
else
  echo "Database population failed or command not found"
  exit 1
fi

if poetry run python manage.py runserver 0.0.0.0:8000; then
  echo "Server started on port 8000"
else
  echo "Server failed to start"
  exit 1
fi
