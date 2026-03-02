import Header from '../components/Header';
import Navbar from '../components/Navbar';

function PreviewOfMeasurements() {
    return (
        <div className="page-2">
            <Navbar variant="patient" profileName="Matej Bohaty" profileRole="Pacient"/>
            <div style={{ padding: 24 }}>
                <Header title="Prehľad meraní" />
            </div>


        </div>
    );
}

export default PreviewOfMeasurements;