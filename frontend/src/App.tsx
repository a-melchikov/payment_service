import { AnimatePresence } from "framer-motion";
import { useEffect, useState } from "react";
import { Route, Routes, useLocation } from "react-router-dom";
import "./App.css";
import BankCardForm from "./BankCardForm/BankCardForm";
import PaymentMethods from "./PaymentMethods/PaymentMethods";
import PaymentStatusPage from "./PaymentStatusPage/PaymentStatusPage";
import TokenValidation from "./components/TokenValidation";
import fetchPaymentData from "./utils/fetchPaymentData";

interface IPaymentData {
	user_id: string;
	total_price: string;
	payment_token: string;
}

function App() {
	const location = useLocation();
	const [paymentData, setPaymentData] = useState<IPaymentData>();

	useEffect(() => {
		const loadPaymentData = async () => {
			try {
				const data = await fetchPaymentData();
				if (data) {
					setPaymentData(data);
					sessionStorage.setItem("token", data.payment_token);
					sessionStorage.setItem("userId", data.user_id);
				}
			} catch (error) {
				console.error("Error fetching payment data:", error);
			}
		};

		loadPaymentData();
	}, []);

	return (
		<AnimatePresence mode="wait">
			<Routes location={location} key={location.pathname}>
				<Route
					path="/"
					element={
						<TokenValidation paymentData={paymentData}>
							<PaymentMethods paymentData={paymentData} />
						</TokenValidation>
					}
				/>
				<Route
					path="bankCard"
					element={
						<TokenValidation paymentData={paymentData}>
							<BankCardForm />
						</TokenValidation>
					}
				/>
				<Route
					path="/bankCard/status"
					element={
						<TokenValidation paymentData={paymentData}>
							<PaymentStatusPage />
						</TokenValidation>
					}
				/>
			</Routes>
		</AnimatePresence>
	);
}

export default App;
