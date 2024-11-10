import { motion } from "framer-motion";
import { useEffect, useRef, useState } from "react";
import { FaAngleLeft, FaRegTrashCan } from "react-icons/fa6";
import { Link, useNavigate } from "react-router-dom";
import bankCard from "../images/bank-card.svg";
import spb from "../images/sbp.svg";
import yoomoney from "../images/YooMoney.svg";
import MethodCard from "../MethodCard/MethodCard";
import { formatAmount } from "../utils/formatAmount";

function PaymentMethods() {
	const [paymentAmount, setPaymentAmount] = useState("0");
	const [containerWidth, setContainerWidth] = useState<number | null>(null);
	const [savedCards, setSavedCards] = useState([]);
	const [userId, setUserId] = useState<string | null>(null); // Состояние для userId
	const containerRef = useRef<HTMLDivElement | null>(null);
	const navigate = useNavigate();

	useEffect(() => {
		if (containerRef.current) {
			setContainerWidth(containerRef.current.offsetWidth);
		}
	}, []);

	useEffect(() => {
		const fetchPaymentAmount = async () => {
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
				setPaymentAmount(data.total_price);

				setUserId(data.user_id);
			} catch (error) {
				console.error("Error fetching payment amount:", error);
			}
		};

		fetchPaymentAmount();
	}, []);

	useEffect(() => {
		if (userId) {
			const fetchSavedCards = async () => {
				try {
					const response = await fetch(
						`http://localhost:8080/api/v1/saved-cards/${userId}`,
						{
							method: "GET",
							headers: {
								"Content-Type": "application/json",
							},
						}
					);

					if (!response.ok) {
						throw new Error("Не удалось загрузить сохранённые карты");
					}

					const cards = await response.json();
					setSavedCards(cards);
				} catch (error) {
					console.error("Error fetching saved cards:", error);
				}
			};

			fetchSavedCards();
		}
	}, [userId]);

	const deleteCard = async (cardNumber: string, event: React.MouseEvent) => {
		event.stopPropagation();

		if (!userId) return;

		try {
			const response = await fetch(`http://localhost:8080/api/v1/saved-cards`, {
				method: "DELETE",
				headers: {
					"Content-Type": "application/json",
				},
				body: JSON.stringify({
					cardNumber: cardNumber,
					userId: userId,
				}),
			});

			if (!response.ok) {
				throw new Error("Не удалось удалить карту");
			}

			setSavedCards(
				savedCards.filter((card) => card.cardNumber !== cardNumber)
			);
		} catch (error) {
			console.error("Error deleting card:", error);
		}
	};

	const paySavedCard = async (cardNumber: string) => {
		if (!userId) return;

		try {
			const response = await fetch(
				`http://localhost:8080/api/v1/saved-cards/pay?paymentSum=${paymentAmount}`,
				{
					method: "POST",
					headers: {
						"Content-Type": "application/json",
					},
					body: JSON.stringify({
						cardNumber: cardNumber,
						userId: userId,
					}),
				}
			);
			const result = await response.json();
			sessionStorage.setItem("paymentStatus", JSON.stringify(result));
			navigate("/bankCard/status");
		} catch (error) {
			console.error("Error paying", error);
		}
	};

	const handleGoBack = () => {
		navigate(-1);
	};

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
				<div
					className="flex self-start items-center gap-2 cursor-pointer"
					onClick={handleGoBack}
				>
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

				{savedCards.length > 0 && (
					<div className="relative mt-4 w-full">
						<p className="font-semibold">Сохранённые карты:</p>
						<ul className="mt-2 border rounded bg-white shadow-md">
							{savedCards.map((card) => (
								<li
									key={card.cardNumber}
									className="px-4 py-2 flex justify-between items-center hover:bg-gray-100 cursor-pointer"
									onClick={() => paySavedCard(card.cardNumber)}
								>
									<span>{card.cardNumber}</span>
									<button
										onClick={(event) => deleteCard(card.cardNumber, event)}
										className="text-red-500 hover:text-red-700 p-2 block"
									>
										<FaRegTrashCan />
									</button>
								</li>
							))}
						</ul>
					</div>
				)}

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
