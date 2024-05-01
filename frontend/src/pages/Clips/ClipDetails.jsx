import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import useFetch from '../../useFetch';


function ClipDetails() {
    const [clipDetails, setClipDetails] = useState()

    const { data, isLoading, error, fetchData } = useFetch(`http://127.0.0.1:5000/favorite_clip_details/${clipId}`);

    const clipId = useParams().id;

    useEffect(() => {
        fetchData(`http://127.0.0.1:5000/favorite_clip_details/${clipId}`)
    }, []);
  
    useEffect(() => {
        if (data) {
            console.log(data)
        }
    }, [data]);
    
    return(
        <>
            <div>
                Clip Details
                
            </div>
            {clipDetails && (
                
                <div>
                    <iframe
                        className="clip-embed"
                        src={`${clipDetails.clip_url_embed}&parent=localhost`}
                        height="900px"
                        width="100%"
                        allowFullScreen>
                    </iframe>
                </div>
            )}       
        </>
    )
}

export default ClipDetails;