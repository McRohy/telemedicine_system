import './Navbar.css';
import { IoPersonCircle, IoPeople } from "react-icons/io5";
import { NavLink } from 'react-router-dom';

function Navbar() {
    return (
        <nav className="navbar">
            <div className='profile'>
                <IoPersonCircle size={80}/>
                 <div>
                <h2>Matej Bohaty</h2>
                <p>Admin</p>
                </div>
            </div>
            <div className="nav-menu">
                <NavLink to="/doctors" className={({isActive}) => `nav-item ${isActive ? 'active' : ''}`}>
                    <IoPeople size={24}/>
                    <span className="nav-link">Prehľad lekárov</span>
                </NavLink>

                <NavLink to="/patients" className={({isActive}) => `nav-item ${isActive ? 'active' : ''}`}>
                    <IoPeople size={24}/>
                    <span className="nav-link">Prehľad pacientov</span>
                </NavLink>   
            </div>
            <a href="/login" className="logout">Odhlásiť sa</a>
        </nav>
    );
}

export default Navbar;