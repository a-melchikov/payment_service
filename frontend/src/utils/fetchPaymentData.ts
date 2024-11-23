const fetchPaymentData = async () => {
	try {
		const response = await fetch("http://localhost:8000/carts/checkout/", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},
			credentials: "include",
		});

		if (response.status === 401) {
			throw new Error(
				"Пользователь не авторизован. Пожалуйста, войдите в систему."
			);
		}

		if (!response.ok) {
			throw new Error(
				`Network response was not ok. Status code: ${response.status}`
			);
		}

		const data = await response.json();
		sessionStorage.setItem("paymentData", JSON.stringify(data));
		return data;
	} catch (error) {
		return { user_id: -1, total_price: 0, payment_token: "access-denied" };
	}
};

export default fetchPaymentData;
