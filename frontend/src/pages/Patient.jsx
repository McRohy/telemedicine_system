import "./Preview.css";
import Header from "../components/Header";
import Navbar from "../components/Navbar";

function Patient() {
  return (
    <div className="page-2">
      <Navbar variant="doctor" profileName="Matej Kralovsky" profileRole="Doctor" />
      <div className="content">
        <div className="header-admin">
          <Header title="Prehľad pacienta" />
        </div>
        <div className="no-data">
             :D
        </div>
      </div>
    </div>
  );
}

export default Patient;
