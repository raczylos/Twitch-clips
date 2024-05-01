import ClipList from "./ClipList";


function ForYouClips() {
    const url = 'http://127.0.0.1:5000/favorite_clips/'
    
    return (
        <>
            <ClipList url={url} method="get"/>
        </>
    )
    
    
}

export default ForYouClips;