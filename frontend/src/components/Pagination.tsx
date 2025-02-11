import React, { useState, useEffect, useRef } from "react";

type Props = {
	currentPage: number;
	totalPages: number;
	onPageChange: (newPage: number) => void;
};

const Pagination = ({ currentPage, totalPages, onPageChange }: Props) => {
	const [maxVisiblePages, setMaxVisiblePages] = useState(11);
	const [editingEllipsis, setEditingEllipsis] = useState<"left" | "right" | null>(null);
	const [customPage, setCustomPage] = useState<string>("");
	const inputRef = useRef<HTMLInputElement | null>(null);

	useEffect(() => {
		const updateMaxVisiblePages = () => {
			if (window.innerWidth < 640) {
				setMaxVisiblePages(7);
			} else if (window.innerWidth < 1024) {
				setMaxVisiblePages(9);
			} else {
				setMaxVisiblePages(11);
			}
		};

		updateMaxVisiblePages();
		window.addEventListener("resize", updateMaxVisiblePages);

		return () => {
			window.removeEventListener("resize", updateMaxVisiblePages);
		};
	}, []);

	const handlePageChange = (page: number) => {
		if (page >= 0 && page < totalPages) {
			onPageChange(page);
		}
	};

	const handleCustomPageSubmit = () => {
		const page = parseInt(customPage, 10) - 1;
		if (page >= 0 && page < totalPages) {
			onPageChange(page);
			setEditingEllipsis(null);
			setCustomPage("");
		}
	};

	const handleClickEllipsis = (ellipsis: "left" | "right") => {
		setEditingEllipsis(ellipsis);
	};

	const renderPageNumbers = () => {
		const pages: (number | "left" | "right")[] = [];

		// show all pages if there are less pages than maxVisiblePages
		if (totalPages <= maxVisiblePages) {
			return Array.from({ length: totalPages }, (_, i) => i);
		}

		if (maxVisiblePages === 7) {
			if (currentPage <= 2) {
				pages.push(0, 1, 2, 3, "right", totalPages - 2, totalPages - 1);
			} else if (currentPage >= totalPages - 3) {
				pages.push(0, 1, "left", totalPages - 4, totalPages - 3, totalPages - 2, totalPages - 1);
			} else {
				pages.push(0, "left", currentPage - 1, currentPage, currentPage + 1, "right", totalPages - 1);
			}
		}

		if (maxVisiblePages === 9) {
			if (currentPage <= 3) {
				pages.push(...Array.from({ length: 6 }, (_, i) => i), "right", totalPages - 2, totalPages - 1);
			} else if (currentPage >= totalPages - 4) {
				pages.push(0, 1, "left", ...Array.from({ length: 6 }, (_, i) => totalPages - 6 + i));
			} else {
				pages.push(0, 1, "left", currentPage - 1, currentPage, currentPage + 1, "right", totalPages - 2, totalPages - 1);
			}
		}

		if (maxVisiblePages === 11) {
			if (currentPage <= 6) {
				pages.push(...Array.from({ length: 8 }, (_, i) => i), "right", totalPages - 2, totalPages - 1);
			} else if (currentPage >= totalPages - 7) {
				pages.push(0, 1, "left", ...Array.from({ length: 8 }, (_, i) => totalPages - 8 + i));
			} else {
				pages.push(
					0,
					1,
					"left",
					currentPage - 2,
					currentPage - 1,
					currentPage,
					currentPage + 1,
					currentPage + 2,
					"right",
					totalPages - 2,
					totalPages - 1
				);
			}
		}

		return pages;
	};

	return (
		<>
			{totalPages > 1 && (
				<nav className="flex justify-center items-center gap-2">
					<button
						onClick={() => handlePageChange(currentPage - 1)}
						disabled={currentPage === 0}
						className="px-3 py-1 border rounded disabled:opacity-50"
					>
						{window.innerWidth < 640 ? "←" : "Previous"}
					</button>

					{renderPageNumbers().map((page, index) => {
						const key = page === "left" || page === "right" ? `${page}-${index}` : `page-${page}`;

						if (page === "left" || page === "right") {
							return (
								<div key={key} className="relative">
									{editingEllipsis === page ? (
										<input
											ref={inputRef}
											type="number"
											value={customPage}
											onChange={(e) => setCustomPage(e.target.value)}
											onBlur={handleCustomPageSubmit}
											onKeyDown={(e) => e.key === "Enter" && handleCustomPageSubmit()}
											className="w-16 text-center border rounded px-2 py-1 bg-[#1a1a1a] text-white outline-none"
											min={1}
											max={totalPages}
											autoFocus
										/>
									) : (
										<button
											onClick={() => handleClickEllipsis(page)}
											className="px-3 py-1 border rounded bg-[#1a1a1a] text-white"
										>
											...
										</button>
									)}
								</div>
							);
						} else {
							return (
								<button
									key={key}
									onClick={() => handlePageChange(page)}
									className={`px-3 py-1 border rounded ${currentPage === page ? "bg-blue-500 text-white" : ""}`}
								>
									{page + 1}
								</button>
							);
						}
					})}

					<button
						onClick={() => handlePageChange(currentPage + 1)}
						disabled={currentPage === totalPages - 1}
						className="px-3 py-1 border rounded disabled:opacity-50"
					>
						{window.innerWidth < 640 ? "→" : "Next"}
					</button>
				</nav>
			)}
		</>
	);
};

export default Pagination;
