import json
import time
import psycopg2

from django.db import transaction
from carts.models import Cart


def wait_for_db():
    while True:
        try:
            conn = psycopg2.connect(
                dbname="backend",
                user="postgres",
                password="postgres",
                host="postgresql",
                port=5432
            )
            conn.close()
            break
        except psycopg2.OperationalError:
            print("Waiting for database to be ready...")
            time.sleep(5)


def listen_to_successful_transactions():
    wait_for_db()
    conn = psycopg2.connect(
        dbname="backend",
        user="postgres",
        password="postgres",
        host="postgresql",
        port=5432
    )
    conn.set_isolation_level(psycopg2.extensions.ISOLATION_LEVEL_AUTOCOMMIT)
    cur = conn.cursor()
    cur.execute("LISTEN successful_transaction;")
    print("Listening to successful_transaction notifications...")

    while True:
        conn.poll()
        while conn.notifies:
            notify = conn.notifies.pop(0)
            data = notify.payload
            process_transaction(data)


def process_transaction(data):
    data = json.loads(data)
    user_id = data.get("user_id")

    if user_id:
        print(f"Clearing cart for user_id={user_id}")
        with transaction.atomic():
            Cart.objects.filter(user_id=user_id).delete()
