import React, { useState, useRef, useEffect } from "react";

type Props = {
  currentPage: number;
  totalPages: number;
  onPageChange: (newPage: number) => void;
};

const Pagination = ({ currentPage, totalPages, onPageChange }: Props) => {
  const [customPageLeft, setCustomPageLeft] = useState("");
  const [customPageRight, setCustomPageRight] = useState("");
  const [editing, setEditing] = useState<"left" | "right" | null>(null);
  const inputRef = useRef<HTMLInputElement | null>(null);

  const handlePageChange = (page: number) => {
    if (page >= 1 && page <= totalPages) {
      onPageChange(page);
    }
  };

  const handleCustomPageSubmit = () => {
    const page = Number(editing === "left" ? customPageLeft : customPageRight);
    if (page >= 1 && page <= totalPages) {
      onPageChange(page);
    }
    setEditing(null);
    setCustomPageLeft("");
    setCustomPageRight("");
  };

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (inputRef.current && !inputRef.current.contains(event.target as Node)) {
        setEditing(null);
        setCustomPageLeft("");
        setCustomPageRight("");
      }
    };

    if (editing) {
      document.addEventListener("mousedown", handleClickOutside);
    } else {
      document.removeEventListener("mousedown", handleClickOutside);
    }

    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [editing]);

  const renderPageNumbers = () => {
    const pages: (number | "left" | "right")[] = [];
    const maxVisiblePages = 11;

    if (totalPages <= maxVisiblePages) {
      return Array.from({ length: totalPages }, (_, i) => i + 1);
    }

    if (currentPage <= 8) {
      pages.push(...Array.from({ length: 9 }, (_, i) => i + 1), "right", totalPages - 1, totalPages);
    } else if (currentPage >= totalPages - 7) {
      pages.push(1, 2, "left", ...Array.from({ length: 9 }, (_, i) => totalPages - 8 + i));
    } else {
      pages.push(1, 2, "left", currentPage - 2, currentPage - 1, currentPage, currentPage + 1, currentPage + 2, "right", totalPages - 1, totalPages);
    }

    return pages;
  };

  return (
    <nav className="flex justify-center items-center gap-2">
      <button
        onClick={() => handlePageChange(currentPage - 1)}
        disabled={currentPage === 1}
        className="px-3 py-1 border rounded disabled:opacity-50"
      >
        Previous
      </button>

      {renderPageNumbers().map((page, index) =>
        page === "left" || page === "right" ? (
          <div key={index} className="relative">
            {editing === page ? (
              <input
                ref={inputRef}
                type="number"
                value={page === "left" ? customPageLeft : customPageRight}
                onChange={(e) =>
                  page === "left" ? setCustomPageLeft(e.target.value) : setCustomPageRight(e.target.value)
                }
                onKeyDown={(e) => e.key === "Enter" && handleCustomPageSubmit()}
                className="w-16 text-center border rounded px-2 py-1 bg-[#1a1a1a] text-white outline-none"
                min={1}
                max={totalPages}
                autoFocus
              />
            ) : (
              <button
                onClick={() => setEditing(page)}
                className="px-3 py-1 border rounded bg-[#1a1a1a] text-white"
              >
                ...
              </button>
            )}
          </div>
        ) : (
          <button
            key={page}
            onClick={() => handlePageChange(page)}
            className={`px-3 py-1 border rounded ${
              currentPage === page ? "bg-blue-500 text-white" : ""
            }`}
          >
            {page}
          </button>
        )
      )}

      <button
        onClick={() => handlePageChange(currentPage + 1)}
        disabled={currentPage === totalPages}
        className="px-3 py-1 border rounded disabled:opacity-50"
      >
        Next
      </button>
    </nav>
  );
};

export default Pagination;
