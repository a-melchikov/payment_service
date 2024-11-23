import datetime
import jwt

from django.conf import settings
from django.shortcuts import get_object_or_404, redirect
from django.urls import reverse_lazy
from django.http import JsonResponse, HttpResponseRedirect
from django.views import View
from django.views.generic import ListView, CreateView, DeleteView, TemplateView
from django.contrib.auth.mixins import LoginRequiredMixin
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator

from products.models import Product
from .models import Cart


class CartListView(LoginRequiredMixin, ListView):
    model = Cart
    template_name = "carts/cart_list.html"
    context_object_name = "cart_items"

    def get_queryset(self):
        return Cart.objects.filter(user=self.request.user)

    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)
        cart_items = self.get_queryset()
        total_price = sum(item.product.price * item.quantity for item in cart_items)
        context["total_price"] = total_price
        return context


class AddToCartView(LoginRequiredMixin, CreateView):
    model = Cart
    fields = ["quantity"]

    def form_valid(self, form):
        form.instance.user = self.request.user
        product = get_object_or_404(Product, pk=self.kwargs["pk"])
        form.instance.product = product
        quantity_to_add = form.cleaned_data["quantity"]

        existing_cart_item = Cart.objects.filter(
            user=self.request.user, product=product
        ).first()

        if existing_cart_item:
            new_quantity = existing_cart_item.quantity + quantity_to_add
            if new_quantity > product.stock:
                return JsonResponse({
                    'success': False, 
                    'error': f"Доступно только {product.stock} штук на складе, у вас в корзине: {existing_cart_item.quantity}."
                })

            existing_cart_item.quantity = new_quantity
            existing_cart_item.save()
            response_data = {'success': True, 'action': 'updated', 'new_quantity': new_quantity}
        else:
            if quantity_to_add > product.stock:
                return JsonResponse({
                    'success': False, 
                    'error': f"Доступно только {product.stock} штук на складе."
                })

            form.instance.quantity = quantity_to_add
            form.instance.save()
            response_data = {'success': True, 'action': 'added', 'new_quantity': quantity_to_add}

        if self.request.headers.get('x-requested-with') == 'XMLHttpRequest':
            return JsonResponse(response_data)
        else:
            return redirect(product.get_absolute_url())

    def form_invalid(self, form):
        return JsonResponse({'success': False, 'error': 'Некорректные данные.'})



class RemoveFromCartView(LoginRequiredMixin, DeleteView):
    model = Cart
    success_url = reverse_lazy("cart-list")

    def get_queryset(self):
        return Cart.objects.filter(user=self.request.user)


@method_decorator(csrf_exempt, name='dispatch')
class CheckoutView(LoginRequiredMixin, View):
    def post(self, request, *args, **kwargs):
        if not request.user.is_authenticated:
            return JsonResponse({'error': 'Требуется аутентификация'}, status=401)

        cart_items = Cart.objects.filter(user=request.user)

        if not cart_items.exists():
            return JsonResponse({'error': 'Корзина пуста'}, status=400)

        total_price = sum(item.product.price * item.quantity for item in cart_items)

        payload = {
            'sub': request.user.id,
            'exp': datetime.datetime.utcnow() + datetime.timedelta(minutes=5)
        }
        token = jwt.encode(payload, settings.SECRET_KEY, algorithm='HS256')

        response_data = {
            'user_id': request.user.id,
            'total_price': total_price,
            'payment_token': token,
        }

        return JsonResponse(response_data, status=200)


class UpdateCartView(View):
    def post(self, request, item_id):
        cart_item = get_object_or_404(Cart, id=item_id, user=request.user)

        try:
            new_quantity = int(request.POST.get("quantity", cart_item.quantity))

            if new_quantity <= 0:
                cart_item.delete()
                return JsonResponse({'success': True, 'action': 'deleted'})  # Удалили товар
            elif new_quantity <= cart_item.product.stock:
                cart_item.quantity = new_quantity
                cart_item.save()
                return JsonResponse({'success': True, 'action': 'updated', 'new_quantity': new_quantity})
        except ValueError:
            pass
        return JsonResponse({'success': False, 'error': 'Invalid quantity'})


