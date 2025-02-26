from django.conf import settings
from django.conf.urls.static import static
from django.contrib import admin
from django.urls import include, path
from django.views.generic import RedirectView


urlpatterns = [
    path("admin/", admin.site.urls),
    path("", RedirectView.as_view(url="products/")),
    path("", include("products.urls")),
    path("users/", include("users.urls")),
    path("carts/", include("carts.urls")),
]

if settings.DEBUG:
    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
