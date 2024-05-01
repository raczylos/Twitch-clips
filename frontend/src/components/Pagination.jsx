import React from 'react';

const Pagination = ({ currentPage, totalPages, onPageChange }) => {
    
    const handlePageChange = (page) => {
        if (page >= 1 && page <= totalPages) {
            onPageChange(page);
        }
    };

    return (
        <nav>
            <ul className="flex list-none">
                <li>
                    <button
                        onClick={() => handlePageChange(currentPage - 1)}
                        disabled={currentPage === 1}
                    >
                        Previous
                    </button>
                </li>
                {Array.from({ length: totalPages }, (_, index) => index + 1).map((page) => (
                    <li key={page}>
                        <button
                            onClick={() => handlePageChange(page)}
                            className={`${currentPage === page ? 'bg-blue-500 ' : ''}`}
                        >
                            {page}
                        </button>
                    </li>
                ))}
                <li>
                    <button
                        onClick={() => handlePageChange(currentPage + 1)}
                        disabled={currentPage === totalPages}
                    >
                        Next
                    </button>
                </li>
            </ul>
        </nav>
        
    );
};

export default Pagination;