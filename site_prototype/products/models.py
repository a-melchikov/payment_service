import io

from PIL import Image

import numpy as np
from django.db import models
from django.urls import reverse


class Category(models.Model):
    name = models.CharField(max_length=255, verbose_name="Название категории")
    slug = models.SlugField(unique=True, verbose_name="Слаг категории")

    def __str__(self):
        return self.name

    class Meta:
        verbose_name = "Категория"
        verbose_name_plural = "Категории"
        ordering = ["name"]


class Product(models.Model):
    name = models.CharField(max_length=255, verbose_name="Название продукта")
    slug = models.SlugField(unique=True, verbose_name="Слаг продукта")
    description = models.TextField(verbose_name="Описание продукта")
    price = models.DecimalField(max_digits=10, decimal_places=2, verbose_name="Цена")
    stock = models.PositiveIntegerField(default=0, verbose_name="Количество на складе")
    category = models.ForeignKey(
        Category,
        related_name="products",
        on_delete=models.CASCADE,
        verbose_name="Категория",
    )
    image = models.ImageField(
        upload_to="products/images/", verbose_name="Изображение продукта"
    )
    created_at = models.DateTimeField(auto_now_add=True, verbose_name="Дата создания")
    updated_at = models.DateTimeField(auto_now=True, verbose_name="Дата обновления")

    def get_absolute_url(self):
        return reverse('product-detail', kwargs={'slug': self.slug})

    def save(self, *args, process_image=True, **kwargs):
        super().save(*args, **kwargs)

        if not self.image or not self.image.path:
            return

        if not process_image:
            return

        img = Image.open(self.image.path).convert("RGBA")
        data = np.array(img)

        white_background = np.ones(data.shape, dtype=np.uint8) * 255
        mask = (data[:, :, :3] < 200).any(axis=2)
        white_background[mask] = data[mask]

        img_no_bg = Image.fromarray(white_background)

        output_size = (400, 400)
        img_no_bg.thumbnail(output_size)

        img_byte_arr = io.BytesIO()
        img_no_bg.save(img_byte_arr, format="PNG")
        img_byte_arr.seek(0)

        if self.image:
            self.image.delete(save=False)

        self.image.save(f"{self.slug}.png", img_byte_arr, save=False)

        super().save(*args, **kwargs)

    def __str__(self):
        return self.name

    class Meta:
        verbose_name = "Продукт"
        verbose_name_plural = "Продукты"
        ordering = ["name"]
