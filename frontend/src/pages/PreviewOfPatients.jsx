import "./Preview.css";
import Header from "../components/Header";
import Navbar from "../components/Navbar";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

function PreviewOfPatients() {
  const [patients, setPatients] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    async function loadPatients() {
      const res = await fetch("http://localhost:8080/api/patients/all");
      const data = await res.json();
      setPatients(data);
    }
    loadPatients();
  }, []);

  if (patients.length === 0) {
    return (
         <div className="page-2">
      <Navbar variant="admin" profileName="Matej Kralovsky" profileRole="Admin" />
      <div className="content">
        <div className="header-admin">
          <Header title="Prehľad pacientov" />
          <button className="btn btn-primary" onClick={() => navigate("/add-patient")}>Pridať pacienta </button>
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
      <Navbar variant="admin" profileName="Matej Kralovsky" profileRole="Admin" />
      <div className="content">
        <div className="header-admin">
          <Header title="Prehľad pacientov" />
          <button className="btn btn-primary" onClick={() => navigate("/add-patient")}>Pridať pacienta </button>
        </div>

        <table className="table">
          <thead>
            <tr>
              <th>Rodne cislo</th>  
              <th>Meno</th>
              <th>Priezvisko</th>
              <th>Email</th>
              <th>PAN cislo osetrujuceho lekara</th>
            </tr>
          </thead>
          <tbody>
            {patients.map((patient) => (
              <tr key={patient.personalNumber}>
                <td>{patient.personalNumber}</td>
                <td>{patient.personalData.firstName}</td>
                <td>{patient.personalData.lastName}</td>
                <td>{patient.personalData.email}</td>
                <td>{patient.doctorPanNumber}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default PreviewOfPatients;
