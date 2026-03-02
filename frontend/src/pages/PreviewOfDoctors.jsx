import Header from '../components/Header';
import Navbar from '../components/Navbar';

function PreviewOfDoctors() {
    return (
        <div className="page-2">
            <Navbar />
            <div style={{ padding: 24 }}>
                <Header title="Prehľad lekárov" />
            </div>
            
            
        </div>
    );
}

export default PreviewOfDoctors;