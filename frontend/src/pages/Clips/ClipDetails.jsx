import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

function ClipDetails() {
    const [clipDetails, setClipDetails] = useState()

    const clipId = useParams().id;
  
    useEffect(() => {
		fetch(`http://127.0.0.1:5000/favorite_clip_details/${clipId}`)
			.then((response) => response.json())
			.then((json) => setClipDetails(json))
			.catch((error) => console.error(error));
	}, []); 
    
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