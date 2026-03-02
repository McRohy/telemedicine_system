import Navbar from '../components/Navbar';
import Header from '../components/Header';

function DoctorPreviewOfPatients() {
    return (
        <div className="page-2">
            <Navbar variant="doctor" profileName="Matej Kralovsky" profileRole="Doctor"/>
            <div style={{ padding: 24 }}>
                <Header title="Prehľad pacientov" />
            </div>
        </div>
    );
}

export default DoctorPreviewOfPatients;