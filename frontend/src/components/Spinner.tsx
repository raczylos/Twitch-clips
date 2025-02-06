
import Box from '@mui/material/Box';
import LinearProgress from '@mui/material/LinearProgress';
import React from 'react';

const Spinner = () => {
    return (
    <Box sx={{ width: '100%' }}>
        <LinearProgress color="secondary"/>
    </Box>
    );
};

export default Spinner;
