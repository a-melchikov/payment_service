function isPastDate(date: string, currentDate: Date) {
	let [month, year] = date.split("/");
	const inputDate = new Date(parseInt(year) + 2000, parseInt(month) - 1);

	return inputDate < currentDate;
}

export default isPastDate;
