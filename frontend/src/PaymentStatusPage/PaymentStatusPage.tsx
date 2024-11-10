import { motion } from "framer-motion";
import { useEffect, useState } from "react";
import { FaCheck, FaExclamation } from "react-icons/fa6";

interface IPaymentStatus {
	userId: number;
	cardNumber: string;
	responseStatus: {
		status: string;
		message: string;
	};
}

function PaymentStatusPage() {
	const [paymentStatus, setPaymentStatus] = useState<IPaymentStatus>();

	useEffect(() => {
		const storedPaymentStatus = sessionStorage.getItem("paymentStatus");
		if (storedPaymentStatus) {
			setPaymentStatus(JSON.parse(storedPaymentStatus));
		}
	}, []);

	const isSuccessful = paymentStatus?.responseStatus.status === "Успех";

	const handleRedirect = () => {
		window.location.href = "http://localhost:8000";
	};

	return (
		<motion.div className="flex self-center justify-center items-center tabletS:mx-10 mobileS:mx-5 max-w-[1200px] w-full tabletS:h-[760px] mobileS:h-[600px] tabletM:rounded-[20px] tabletS:rounded-[16px] mobileM:rounded-[12px] mobileS:rounded-[8px] bg-primary">
			<div className="flex flex-col justify-center items-center w-full h-full gap-10">
				<p
					className={`text-center text-wrap font-semibold mx-3 tabletM:text-[28px] tabletS:text-[24px] mobileM:text-[20px] mobileS:text-[16px] ${
						isSuccessful ? "text-valid" : "text-invalid"
					}`}
				>
					{paymentStatus?.responseStatus.message}
				</p>
				{isSuccessful ? (
					<div className="flex justify-center mobileM:mx-24 mobileS:mx-16 items-center tabletS:max-w-[250px] mobileM:max-w-[190px] mobileS:max-w-[150px] w-full h-auto tabletS:border-[12px] mobileS:border-[8px] border-valid rounded-[50%] aspect-[1/1]">
						<FaCheck className="tabletS:text-[96px] mobileM:text-[72px] mobileS:text-[48px] text-valid" />
					</div>
				) : (
					<div className="flex justify-center mobileM:mx-24 mobileS:mx-16 items-center tabletS:max-w-[250px] mobileM:max-w-[190px] mobileS:max-w-[150px] w-full h-auto tabletS:border-[12px] mobileS:border-[8px] border-invalid rounded-[50%] aspect-[1/1]">
						<FaExclamation className="tabletS:text-[96px] mobileM:text-[72px] mobileS:text-[48px] text-invalid" />
					</div>
				)}
				<button
					className="tabletS:w-[275px] tabletS:h-[60px] mobileS:w-[150px] mobileS:h-[40px] bg-primaryBtn shadow-btnShadow rounded-[15px] tabletS:text-[22px] mobileS:text-[13px]"
					onClick={handleRedirect}
				>
					Вернуться в магазин
				</button>
			</div>
		</motion.div>
	);
}

export default PaymentStatusPage;
