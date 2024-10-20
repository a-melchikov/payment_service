from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from .models import CustomUser


class CustomUserAdmin(UserAdmin):
    model = CustomUser
    list_display = (
        "username",
        "email",
        "first_name",
        "last_name",
        "is_staff",
        "birth_date",
        "date_joined",
    )
    fieldsets = UserAdmin.fieldsets + ((None, {"fields": ("birth_date",)}),)
    add_fieldsets = UserAdmin.add_fieldsets + ((None, {"fields": ("birth_date",)}),)


admin.site.register(CustomUser, CustomUserAdmin)
