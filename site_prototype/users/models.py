from django.db import models
from django.contrib.auth.models import AbstractUser


class CustomUser(AbstractUser):
    birth_date = models.DateField(blank=True, null=True, verbose_name="Дата рождения")
    date_joined = models.DateTimeField(
        auto_now_add=True, verbose_name="Дата регистрации"
    )

    def __str__(self):
        return self.username

    class Meta:
        verbose_name = "Пользователь"
        verbose_name_plural = "Пользователи"
        ordering = ["-date_joined"]
