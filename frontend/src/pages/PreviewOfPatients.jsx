import Navbar from '../components/Navbar';
import Header from '../components/Header';

function PreviewOfPatients() {
    return (
        <div className="page-2">
            <Navbar variant="admin" profileName="Matej Kralovsky" profileRole="Admin"/>
            <div style={{ padding: 24 }}>
                <Header title="Prehľad pacientov" />
            </div>
        </div>
    );
}

export default PreviewOfPatients;