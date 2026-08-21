import { useState } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import { AppBar, Avatar, Badge, Box, Drawer, IconButton, InputBase, List, ListItemButton, ListItemIcon, ListItemText, Toolbar, Typography } from '@mui/material';
import DashboardRounded from '@mui/icons-material/DashboardRounded';
import GroupsRounded from '@mui/icons-material/GroupsRounded';
import PriceChangeRounded from '@mui/icons-material/PriceChangeRounded';
import AccountBalanceRounded from '@mui/icons-material/AccountBalanceRounded';
import Inventory2Rounded from '@mui/icons-material/Inventory2Rounded';
import ReceiptLongRounded from '@mui/icons-material/ReceiptLongRounded';
import AssessmentRounded from '@mui/icons-material/AssessmentRounded';
import AdminPanelSettingsRounded from '@mui/icons-material/AdminPanelSettingsRounded';
import SettingsRounded from '@mui/icons-material/SettingsRounded';
import SearchRounded from '@mui/icons-material/SearchRounded';
import NotificationsNoneRounded from '@mui/icons-material/NotificationsNoneRounded';
import MenuRounded from '@mui/icons-material/MenuRounded';
const items=[['Dashboard','/',DashboardRounded],['Customers','/customers',GroupsRounded],['Pricing','/pricing',PriceChangeRounded],['Billing Accounts','/billing-accounts',AccountBalanceRounded],['Products','/products',Inventory2Rounded],['Invoices','/invoices',ReceiptLongRounded],['Reports','/reports',AssessmentRounded],['Administration','/admin',AdminPanelSettingsRounded],['Settings','/settings',SettingsRounded]] as const;
const drawerWidth=260;
export default function AppShell(){
 const [mobileOpen,setMobileOpen]=useState(false); const nav=useNavigate(); const loc=useLocation();
 const drawer=<Box sx={{height:'100%',display:'flex',flexDirection:'column',bgcolor:'#10213F',color:'white'}}><Box sx={{p:3}}><Typography variant="h5">NimbusBill</Typography><Typography variant="caption" sx={{opacity:.7}}>Payment Billing Platform</Typography></Box><List sx={{px:1.5}}>{items.map(([label,path,Icon])=><ListItemButton key={label} selected={loc.pathname===path || (path!=='/'&&loc.pathname.startsWith(path))} onClick={()=>{nav(path);setMobileOpen(false)}} sx={{mb:.5,borderRadius:2,color:'rgba(255,255,255,.82)','&.Mui-selected':{bgcolor:'rgba(255,255,255,.12)',color:'white'},'&:hover':{bgcolor:'rgba(255,255,255,.08)'}}}><ListItemIcon sx={{minWidth:40,color:'inherit'}}><Icon/></ListItemIcon><ListItemText primary={label}/></ListItemButton>)}</List><Box sx={{mt:'auto',p:2}}><Box sx={{p:2,borderRadius:3,bgcolor:'rgba(255,255,255,.08)'}}><Typography fontWeight={700}>Cloud native</Typography><Typography variant="caption" sx={{opacity:.7}}>AWS-ready reference UI</Typography></Box></Box></Box>;
 return <Box sx={{display:'flex',minHeight:'100vh'}}><AppBar position="fixed" color="inherit" elevation={0} sx={{ml:{md:`${drawerWidth}px`},width:{md:`calc(100% - ${drawerWidth}px)`},borderBottom:'1px solid #E7ECF3'}}><Toolbar sx={{gap:2}}><IconButton sx={{display:{md:'none'}}} onClick={()=>setMobileOpen(true)}><MenuRounded/></IconButton><Box sx={{display:'flex',alignItems:'center',bgcolor:'#F1F5F9',px:1.5,py:.5,borderRadius:3,flex:1,maxWidth:520}}><SearchRounded color="action"/><InputBase placeholder="Search customers, invoices, accounts..." sx={{ml:1,flex:1}}/></Box><Badge badgeContent={3} color="error"><NotificationsNoneRounded/></Badge><Avatar sx={{width:36,height:36}}>KR</Avatar></Toolbar></AppBar><Box component="nav" sx={{width:{md:drawerWidth},flexShrink:{md:0}}}><Drawer variant="temporary" open={mobileOpen} onClose={()=>setMobileOpen(false)} ModalProps={{keepMounted:true}} sx={{display:{xs:'block',md:'none'},'& .MuiDrawer-paper':{width:drawerWidth}}}>{drawer}</Drawer><Drawer variant="permanent" open sx={{display:{xs:'none',md:'block'},'& .MuiDrawer-paper':{width:drawerWidth,border:0}}}>{drawer}</Drawer></Box><Box component="main" sx={{flexGrow:1,p:{xs:2,md:3.5},mt:8,width:{md:`calc(100% - ${drawerWidth}px)`}}}><Outlet/></Box></Box>;
}
