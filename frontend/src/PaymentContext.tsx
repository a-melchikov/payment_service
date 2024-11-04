import { createContext, ReactNode, useContext, useState } from "react";

interface PaymentData {
	userId: string;
	paymentAmount: number;
}

interface PaymentStatus {
	userId: number;
	cardNumber: string;
	responseStatus: {
		status: string;
		message: string;
	};
}

interface PaymentContextType {
	paymentData: PaymentData | null;
	paymentStatus: PaymentStatus | null;
	setPaymentData: (data: PaymentData) => void;
	setPaymentStatus: (status: PaymentStatus) => void;
}

const PaymentContext = createContext<PaymentContextType | undefined>(undefined);

export const PaymentProvider = ({ children }: { children: ReactNode }) => {
	const [paymentData, setPaymentData] = useState<PaymentData | null>(null);
	const [paymentStatus, setPaymentStatus] = useState<PaymentStatus | null>(
		null
	);

	return (
		<PaymentContext.Provider
			value={{ paymentData, setPaymentData, paymentStatus, setPaymentStatus }}
		>
			{children}
		</PaymentContext.Provider>
	);
};

export const usePayment = () => {
	const context = useContext(PaymentContext);
	if (!context) {
		throw new Error("usePayment must be used within a PaymentProvider");
	}
	return context;
};
