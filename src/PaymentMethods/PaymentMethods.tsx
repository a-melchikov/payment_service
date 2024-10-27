import { motion } from "framer-motion";
import { useEffect, useRef, useState } from "react";
import { Link } from "react-router-dom";
import MethodCard from "../MethodCard/MethodCard";
import { formatAmount } from "../utils/formatAmount";

import bankCard from "../images/bank-card.svg";
import spb from "../images/sbp.svg";
import yoomoney from "../images/YooMoney.svg";

import { FaAngleLeft } from "react-icons/fa6";

function PaymentMethods() {
	const [paymentAmount, setPaymentAmount] = useState("10");
	const [containerWidth, setContainerWidth] = useState<number | null>(null);
	const containerRef = useRef<HTMLDivElement | null>(null);

	useEffect(() => {
		if (containerRef.current) {
			setContainerWidth(containerRef.current.offsetWidth);
		}
	}, []);

	return (
		<motion.div
			className="flex flex-col self-center tabletS:mx-10 mobileS:mx-5 px-5 pt-8 max-w-[580px] w-full tabletS:h-[760px] mobileS:h-[600px] tabletM:rounded-[20px] tabletS:rounded-[16px] mobileM:rounded-[12px] mobileS:rounded-[8px] bg-primary"
			initial={{ opacity: 0, y: 60 }}
			animate={{ opacity: 1, y: 0 }}
			transition={{ duration: 0.75 }}
			ref={containerRef}
		>
			<motion.div
				exit={{ opacity: 0 }}
				className="flex flex-col flex-grow self-center items-center w-fit h-fit tabletS:gap-[30px] mobileS:gap-[20px]"
			>
				<div className="flex self-start items-center gap-2 cursor-pointer">
					<div className="flex justify-center items-center tabletS:w-[30px] tabletS:h-[30px] mobileS:w-[24px] mobileS:h-[24px] bg-backBtn rounded-[5px]">
						<FaAngleLeft className="tabletS:text-[20px] mobileS:text-[16px]" />
					</div>
					<p className="tabletS:text-[16px] mobileS:text-[12px]">
						Вернуться назад
					</p>
				</div>
				<p className="tabletS:text-[32px] mobileS:text-[24px] font-bold text-center">
					Выберите способ оплаты
				</p>
				<MethodCard imageSource={spb} text="Система быстрых платежей" />
				<Link to="/bankCard" state={{ containerWidth }}>
					<MethodCard imageSource={bankCard} text="Банковская карта" />
				</Link>
				<MethodCard imageSource={yoomoney} text="ЮMoney" />
				<div className="flex flex-col mt-auto w-full mb-12">
					<hr className="border-0 w-full h-[2px] bg-black opacity-30 rounded" />
					<p className="tabletS:text-[32px] mobileS:text-[24px] font-semibold text-center self-end mt-4">
						{formatAmount(paymentAmount)}
					</p>
				</div>
			</motion.div>
		</motion.div>
	);
}

export default PaymentMethods;
