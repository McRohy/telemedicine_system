import "./Preview.css";
import Header from "../components/Header";
import Navbar from "../components/Navbar";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

function PreviewOfDoctors() {
  const [doctors, setDoctors] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://localhost:8080/api/doctors")
      .then((response) => response.json())
      .then((data) => {
        setDoctors(data);
      })
      .catch((error) => {
        console.error("Error fetching doctors:", error);
      });
  }, []);

  return (
    <div className="page-2">
      <Navbar variant="admin" profileName="Matej Kralovsky" profileRole="Admin" />
      <div className="content">
        <div className="header-admin">
          <Header title="Prehľad lekárov" />
          <button className="btn btn-primary" onClick={() => navigate("/add-doctor")}>Pridať lekára </button>
        </div>

        <table className="table">
          <thead>
            <tr>
              <th>PAN cislo</th>
              <th>Meno</th>
              <th>Priezvisko</th>
              <th>Email</th>
              <th>Špecializácia</th>
            </tr>
          </thead>
          <tbody>
            {doctors.map((doctor) => (
              <tr key={doctor.panNumber}>
                <td>{doctor.panNumber}</td>
                <td>{doctor.personalData.firstName}</td>
                <td>{doctor.personalData.lastName}</td>
                <td>{doctor.personalData.email}</td>
                <td>{doctor.specialization}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default PreviewOfDoctors;
