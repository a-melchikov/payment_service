export const formatAmount = (amount: string): string => {
	const regex = /^\d+(\.\d{0,2})?$/;

	if (!regex.test(amount)) {
		return "0.00 р.";
	}

	const numericAmount = parseFloat(amount);
	return `${numericAmount.toFixed(2)} р.`;
};
