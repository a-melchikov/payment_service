from django.views import View
from django.shortcuts import render, redirect
from django.contrib.auth import login, authenticate
from .forms import CustomUserCreationForm, CustomAuthenticationForm


class UserRegisterView(View):
    def get(self, request):
        form = CustomUserCreationForm()
        return render(request, "users/register.html", {"form": form})

    def post(self, request):
        form = CustomUserCreationForm(request.POST)
        if form.is_valid():
            form.save()
            return redirect("login")
        return render(request, "users/register.html", {"form": form})


class UserLoginView(View):
    def get(self, request):
        form = CustomAuthenticationForm()
        return render(request, "users/login.html", {"form": form})

    def post(self, request):
        form = CustomAuthenticationForm(request, data=request.POST)
        if form.is_valid():
            username = form.cleaned_data.get("username")
            password = form.cleaned_data.get("password")
            user = authenticate(request, username=username, password=password)
            if user is not None:
                login(request, user)
                return redirect("product-list")
        return render(request, "users/login.html", {"form": form})
