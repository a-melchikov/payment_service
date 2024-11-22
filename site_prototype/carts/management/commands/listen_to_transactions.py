from django.core.management.base import BaseCommand
from carts.listeners import listen_to_successful_transactions


class Command(BaseCommand):
    help = "Listen to successful transaction notifications"

    def handle(self, *args, **kwargs):
        self.stdout.write("Starting listener for successful transactions...")
        listen_to_successful_transactions()
