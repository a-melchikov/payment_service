import { AnimatePresence } from "framer-motion";
import { Route, Routes, useLocation } from "react-router-dom";
import "./App.css";
import BankCardForm from "./BankCardForm/BankCardForm";
import PaymentMethods from "./PaymentMethods/PaymentMethods";
import PaymentStatusPage from "./PaymentStatusPage/PaymentStatusPage";

function App() {
	const location = useLocation();

	return (
		<AnimatePresence mode="wait">
			<Routes location={location} key={location.pathname}>
				<Route path="/" element={<PaymentMethods />} />
				<Route path="bankCard" element={<BankCardForm />} />
				<Route path="/bankCard/status" element={<PaymentStatusPage />} />
			</Routes>
		</AnimatePresence>
	);
}

export default App;
