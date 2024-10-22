from django.shortcuts import get_object_or_404
from django.views.generic import ListView, DetailView

from carts.models import Cart
from .models import Product, Category


class ProductListView(ListView):
    model = Product
    template_name = "products/product_list.html"
    context_object_name = "products"


class ProductDetailView(DetailView):
    model = Product
    template_name = "products/product_detail.html"
    context_object_name = "product"

    def get_object(self):
        return get_object_or_404(Product, slug=self.kwargs["slug"])

    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)

        user = self.request.user
        product = self.get_object()

        if user.is_authenticated:
            current_quantity = Cart.objects.filter(user=user, product=product).first()
            current_quantity = current_quantity.quantity if current_quantity else 0
        else:
            current_quantity = 0

        max_quantity = product.stock - current_quantity
        context["max_quantity"] = max_quantity
        context["can_add_to_cart"] = max_quantity > 0

        return context


class CategoryListView(ListView):
    model = Category
    template_name = "products/category_list.html"
    context_object_name = "categories"

    def get_queryset(self):
        return Category.objects.all()


class CategoryDetailView(DetailView):
    model = Category
    template_name = "products/category_detail.html"
    context_object_name = "category"

    def get_object(self):
        return Category.objects.get(slug=self.kwargs["slug"])
