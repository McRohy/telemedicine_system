import "./Preview.css";
import Header from "../components/Header";
import Navbar from "../components/Navbar";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

function DoctorPreviewOfPatients() {
  const [patients, setPatients] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    async function loadPatients() {
      const res = await fetch("http://localhost:8080/api/patients?panNumber=9703990654280621");
      const data = await res.json();
      setPatients(data);
    }
    loadPatients();
  }, []);

  if (patients.length === 0) {
    return (
         <div className="page-2">
      <Navbar variant="doctor" profileName="Matej Kralovsky" profileRole="Doctor" />
      <div className="content">
        <div className="header-admin">
          <Header title="Prehľad pacientov" />
          <button className="btn btn-primary" onClick={() => navigate("/add-patient?panNumber=9703990654280621")}>Pridať pacienta </button>
        </div>
        <div className="no-data">
        <p>Žiadni pacienti k dispozícii.</p>
      </div>

        </div>
        </div>
    );
  }

  return (
    <div className="page-2">
      <Navbar variant="doctor" profileName="Matej Kralovsky" profileRole="Doctor" />
      <div className="content">
        <div className="header-admin">
          <Header title="Prehľad pacientov" />
          <button className="btn btn-primary" onClick={() => navigate("/add-patient?panNumber=9703990654280621")}>Pridať pacienta </button>
        </div>

        <table className="table">
          <thead>
            <tr>
              <th>Rodne cislo</th>  
              <th>Meno</th>
              <th>Priezvisko</th>
              <th>Email</th>
            </tr>
          </thead>
          <tbody>
            {patients.map((patient) => (
              <tr key={patient.personalNumber} onClick={() => navigate(`/patient/${patient.personalNumber}`)} style={{ cursor: "pointer" }}>
                <td>{patient.personalNumber}</td>
                <td>{patient.personalData.firstName}</td>
                <td>{patient.personalData.lastName}</td>
                <td>{patient.personalData.email}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default DoctorPreviewOfPatients;
