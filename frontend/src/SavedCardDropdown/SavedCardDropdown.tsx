import { motion } from "framer-motion";
import { useEffect, useState } from "react";
import { FaRegTrashCan } from "react-icons/fa6";
import { useNavigate } from "react-router-dom";

interface IPaymentData {
	user_id: string;
	total_price: string;
}

interface SavedCardsDropdownProps {
	paymentData: IPaymentData | null;
	containerWidth: number | null;
}

interface ICard {
	cardNumber: string;
	user_id: string;
}

function SavedCardsDropdown({
	paymentData,
	containerWidth,
}: SavedCardsDropdownProps) {
	const [isOpen, setIsOpen] = useState(false);
	const [savedCards, setSavedCards] = useState([]);
	const [savedCardNumber, setSavedCardNumber] =
		useState<string>("Сохранённые карты");
	const [isCardSelected, setIsCardSelected] = useState(false);
	const navigate = useNavigate();

	const toggleDropdown = () => setIsOpen(!isOpen);

	useEffect(() => {
		const fetchSavedCards = async () => {
			if (!paymentData) return;

			try {
				const response = await fetch(
					`http://localhost:8080/api/v1/saved-cards?userId=${paymentData.user_id}`,
					{
						method: "GET",
						headers: {
							"Content-Type": "application/json",
							"Authorization": `Bearer ${paymentData?.payment_token}`,
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
	}, [paymentData]);

	const deleteCard = async (cardNumber: string, event: React.MouseEvent) => {
		event.stopPropagation();
		if (!paymentData) return;
		try {
			const response = await fetch(`http://localhost:8080/api/v1/saved-cards?userId=${paymentData.user_id}`, {
				method: "DELETE",
				headers: {
					"Content-Type": "application/json",
					"Authorization": `Bearer ${paymentData?.payment_token}`,
				},
				body: JSON.stringify({
					cardNumber: cardNumber,
					userId: paymentData?.user_id,
				}),
			});

			if (!response.ok) {
				throw new Error("Не удалось удалить карту");
			}

			setSavedCards(
				savedCards.filter((card: ICard) => card.cardNumber !== cardNumber)
			);
			setIsCardSelected(false);
			setSavedCardNumber("Сохранённые карты");
		} catch (error) {
			console.error("Error deleting card:", error);
		}
	};

	const paySavedCard = async (cardNumber: string) => {
		if (!paymentData) return;
		try {
			const response = await fetch(
				`http://localhost:8080/api/v1/saved-cards/pay?paymentSum=${paymentData?.total_price}&userId=${paymentData.user_id}`,
				{
					method: "POST",
					headers: {
						"Content-Type": "application/json",
						"Authorization": `Bearer ${paymentData?.payment_token}`,
					},
					body: JSON.stringify({
						cardNumber: cardNumber,
						userId: paymentData?.user_id,
					}),
				}
			);
			const result = await response.json();
			sessionStorage.setItem("paymentStatus", JSON.stringify(result));
			navigate("/bankCard/status", { state: { containerWidth } });
		} catch (error) {
			console.error("Error paying", error);
		}
	};

	const chooseSavedCard = (cardNumber: string) => {
		setSavedCardNumber(cardNumber);
		setIsOpen(false);
		setIsCardSelected(true);
	};

	return (
		<div
			className={`flex flex-col items-center gap-3 w-full ${
				savedCards.length > 0 ? "" : "hidden"
			}`}
		>
			<div className="relative w-full">
				<button
					onClick={toggleDropdown}
					className="w-full px-4 tabletS:py-2 mobileS:py-1 bg-gray-200 rounded-md text-left flex justify-between items-center"
				>
					<span className="tabletS:text-[16px] mobileS:text-[12px]">
						{savedCardNumber}
					</span>
					<span className="text-gray-500">{isOpen ? "▲" : "▼"}</span>
				</button>

				{isOpen && (
					<motion.ul
						initial={{
							height: "0",
							overflow: savedCards.length > 3 ? "auto" : "hidden",
						}}
						animate={{ height: "auto" }}
						transition={{ duration: 0.3, ease: "easeOut" }}
						className="absolute z-10 w-full tabletS:max-h-[123px] mobileS:max-h-[80px] mt-2 bg-white border rounded-md shadow-lg scrollbar-thin scrollbar-webkit"
					>
						{savedCards.map((card) => (
							<li
								key={card.cardNumber}
								className="flex justify-between items-center px-4 tabletS:py-2 mobileS:py-1 hover:bg-gray-100 cursor-pointer"
								onClick={() => chooseSavedCard(card.cardNumber)}
							>

								<span className="tabletS:text-[16px] mobileS:text-[12px]">
									{card.cardNumber}
								</span>
								<button
									onClick={(e) => deleteCard(card.cardNumber, e)}
									className="text-red-500 hover:text-red-700 tabletS:text-[16px] mobileS:text-[12px]"
								>
									<FaRegTrashCan />
								</button>
							</li>
						))}
					</motion.ul>
				)}
			</div>
			<motion.button
				whileTap={{ scale: 0.95, transition: { duration: 0.1 } }}
				className={`tabletS:w-[180px] tabletS:h-[45px] mobileS:w-[140px] mobileS:h-[35px] bg-primaryBtn tabletM:shadow-btnShadow mobileS:shadow-btnShadowMobile rounded-[15px] tabletS:text-[20px] mobileS:text-[16px] ${
					isCardSelected ? "" : "hidden"
				}`}
				onClick={() => paySavedCard(savedCardNumber)}
			>
				Оплатить
			</motion.button>
		</div>
	);
}

export default SavedCardsDropdown;
