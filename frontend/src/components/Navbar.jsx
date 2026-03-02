import './Navbar.css';
import { IoPersonCircle, IoPeople } from "react-icons/io5";
import { NavLink } from 'react-router-dom';

const menus = {
    admin: [
        { to: '/doctors', icon: <IoPeople size={24} />, label: 'Prehľad lekárov' },
        { to: '/patients', icon: <IoPeople size={24} />, label: 'Prehľad pacientov' },
    ],  
};

function Navbar({variant, profileName, profileRole}) {
    const items = menus[variant];

    return (
        <nav className="navbar">
            <div className='profile'>
                <IoPersonCircle size={80}/>
                 <div>
                <h2>{profileName}</h2>
                <p>{profileRole}</p>
                </div>
            </div>
            <div className="nav-menu">
                {items.map((item) => (
                    <NavLink key={item.to} to={item.to} className={({isActive}) => `nav-item ${isActive ? 'active' : ''}`} >
                        {item.icon}
                        {item.label}
                    </NavLink>
                ))}
            </div>
            <NavLink to="/login" className="logout">Odhlásiť sa</NavLink>
        </nav>
    );
}

export default Navbar;