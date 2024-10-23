import os
from django.core.management.base import BaseCommand

from products.models import Category, Product


class Command(BaseCommand):
    help = "Заполняет базу данных категориями и продуктами"

    def create_categories(self):
        categories_data = [
            {"name": "Шкафы", "slug": "shkafy"},
            {"name": "Столярные изделия", "slug": "stolyarnye-izdeliya"},
            {"name": "Инструменты", "slug": "instrumenty"},
            {"name": "Строительные материалы", "slug": "stroitelnyye-materialy"},
        ]

        for category in categories_data:
            Category.objects.get_or_create(name=category["name"], slug=category["slug"])

    def handle(self, *args, **kwargs):
        self.create_categories()

        category1 = Category.objects.get(slug="shkafy")
        category2 = Category.objects.get(slug="stolyarnye-izdeliya")
        category3 = Category.objects.get(slug="instrumenty")
        category4 = Category.objects.get(slug="stroitelnyye-materialy")

        product_data = [
            {
                "name": "Шкаф угловой",
                "slug": "shkaf-uglovoy",
                "description": "Угловой шкаф для хранения вещей.",
                "price": 25000.00,
                "stock": 10,
                "category": category1,
                "image_path": "products/images/shkaf-uglovoy.png",
            },
            {
                "name": "Шкаф-купе",
                "slug": "shkaf-kupe",
                "description": "Шкаф-купе с зеркальными дверями.",
                "price": 30000.00,
                "stock": 5,
                "category": category1,
                "image_path": "products/images/shkaf-kupe.png",
            },
            {
                "name": "Деревянный стол",
                "slug": "derevyannyy-stol",
                "description": "Стол из натурального дерева.",
                "price": 15000.00,
                "stock": 7,
                "category": category2,
                "image_path": "products/images/derevyannyy-stol.png",
            },
            {
                "name": "Стул с мягкой обивкой",
                "slug": "stul-s-myagkoy-obivkoy",
                "description": "Комфортный стул с мягким сиденьем.",
                "price": 5000.00,
                "stock": 20,
                "category": category2,
                "image_path": "products/images/stul-s-myagkoy-obivkoy.png",
            },
            {
                "name": "Молоток",
                "slug": "molotok",
                "description": "Классический молоток для работы.",
                "price": 300.00,
                "stock": 50,
                "category": category3,
                "image_path": "products/images/molotok.png",
            },
            {
                "name": "Отвертка",
                "slug": "otvertka",
                "description": "Отвертка с сменными насадками.",
                "price": 150.00,
                "stock": 30,
                "category": category3,
                "image_path": "products/images/otvertka.png",
            },
            {
                "name": "Ручная дрель",
                "slug": "ruchnaya-drel",
                "description": "Ручная дрель для бытовых нужд.",
                "price": 1200.00,
                "stock": 15,
                "category": category3,
                "image_path": "products/images/ruchnaya-drel.png",
            },
            {
                "name": "Бетонные блоки",
                "slug": "betonnyye-bloki",
                "description": "Бетонные блоки для строительства.",
                "price": 300.00,
                "stock": 100,
                "category": category4,
                "image_path": "products/images/betonnyye-bloki.png",
            },
            {
                "name": "Цемент",
                "slug": "tsement",
                "description": "Качественный цемент для строительства.",
                "price": 500.00,
                "stock": 200,
                "category": category4,
                "image_path": "products/images/tsement.png",
            },
            {
                "name": "Кирпич",
                "slug": "kirpich",
                "description": "Кирпич для кладки.",
                "price": 25.00,
                "stock": 500,
                "category": category4,
                "image_path": "products/images/kirpich.png",
            },
            {
                "name": "Штукатурка",
                "slug": "shtukaturka",
                "description": "Штукатурка для внутренних работ.",
                "price": 700.00,
                "stock": 150,
                "category": category4,
                "image_path": "products/images/shtukaturka.png",
            },
            {
                "name": "Лестница",
                "slug": "lestnitsa",
                "description": "Удобная лестница для работы на высоте.",
                "price": 3500.00,
                "stock": 10,
                "category": category2,
                "image_path": "products/images/lestnitsa.png",
            },
            {
                "name": "Краска",
                "slug": "kraska",
                "description": "Краска для внутренних и наружных работ.",
                "price": 400.00,
                "stock": 25,
                "category": category4,
                "image_path": "products/images/kraska.png",
            },
            {
                "name": "Пила",
                "slug": "pila",
                "description": "Ручная пила для древесины.",
                "price": 600.00,
                "stock": 20,
                "category": category3,
                "image_path": "products/images/pila.png",
            },
        ]

        for product in product_data:
            new_product = Product(
                name=product["name"],
                slug=product["slug"],
                description=product["description"],
                price=product["price"],
                stock=product["stock"],
                category=product["category"],
            )

            if os.path.exists(f"media/{product["image_path"]}"):
                new_product.image = product["image_path"]
            else:
                self.stdout.write(
                    self.style.WARNING(
                        f"Изображение не найдено: {product['image_path']}"
                    )
                )

            new_product.save(process_image=False)

        self.stdout.write(self.style.SUCCESS("База данных успешно заполнена!"))
