import Header from '../components/Header';
import Navbar from '../components/Navbar';

function PreviewOfDoctors() {
    return (
        <div className="page-2">
            <Navbar variant="admin" profileName="Matej Kralovsky" profileRole="Admin"/>
            <div style={{ padding: 24 }}>
                <Header title="Prehľad lekárov" />
            </div>


        </div>
    );
}

export default PreviewOfDoctors;