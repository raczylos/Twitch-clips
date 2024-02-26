
// import CircularProgress from '@mui/material/CircularProgress';
import Box from '@mui/material/Box';
import LinearProgress from '@mui/material/LinearProgress';



const Spinner = () => {
    return (
    // <div className="spinner">
    //     <LinearProgress />
    //     {/* <CircularProgress /> */}
    // </div>
    <Box sx={{ width: '100%' }}>
        <LinearProgress color="secondary"/>
    </Box>
    );
};

export default Spinner;
