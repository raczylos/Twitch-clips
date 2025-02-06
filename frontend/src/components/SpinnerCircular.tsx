import Box from '@mui/material/Box';
import CircularProgress from '@mui/material/CircularProgress';
import React from 'react';

const SpinnerCircular = () => {
    return (
    <Box sx={{ width: '100%'}}>
        <CircularProgress />
    </Box>
    );
};

export default SpinnerCircular;
