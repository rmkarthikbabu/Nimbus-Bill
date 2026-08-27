import {useEffect,useState} from 'react';
import {Outlet,useLocation,useNavigate} from 'react-router-dom';
import {AppBar,Avatar,Badge,Box,Collapse,Drawer,IconButton,InputBase,List,ListItemButton,ListItemIcon,ListItemText,Toolbar,Typography} from '@mui/material';
import DashboardRounded from '@mui/icons-material/DashboardRounded';
import GroupsRounded from '@mui/icons-material/GroupsRounded';
import PersonAddRounded from '@mui/icons-material/PersonAddRounded';
import TuneRounded from '@mui/icons-material/TuneRounded';
import PriceChangeRounded from '@mui/icons-material/PriceChangeRounded';
import AccountBalanceRounded from '@mui/icons-material/AccountBalanceRounded';
import Inventory2Rounded from '@mui/icons-material/Inventory2Rounded';
import ReceiptLongRounded from '@mui/icons-material/ReceiptLongRounded';
import SwapHorizRounded from '@mui/icons-material/SwapHorizRounded';
import OutboxRounded from '@mui/icons-material/OutboxRounded';
import AssessmentRounded from '@mui/icons-material/AssessmentRounded';
import AdminPanelSettingsRounded from '@mui/icons-material/AdminPanelSettingsRounded';
import SettingsRounded from '@mui/icons-material/SettingsRounded';
import SearchRounded from '@mui/icons-material/SearchRounded';
import NotificationsNoneRounded from '@mui/icons-material/NotificationsNoneRounded';
import MenuRounded from '@mui/icons-material/MenuRounded';
import ExpandLessRounded from '@mui/icons-material/ExpandLessRounded';
import ExpandMoreRounded from '@mui/icons-material/ExpandMoreRounded';

const drawerWidth=280;
const customerPaths=['/customers','/customers/new','/customer-configuration'];
const pricingPaths=['/pricing','/pricing-editor','/approval-queue','/pricing-versions','/pricing-overrides','/products'];
const primaryItems=[['Dashboard','/',DashboardRounded],['Transactions','/transactions',SwapHorizRounded],['Event Outbox','/transaction-outbox',OutboxRounded],['Internal Transfers','/internal-transfers',AccountBalanceRounded],['Billing Accounts','/billing-accounts',AccountBalanceRounded],['Invoices','/invoices',ReceiptLongRounded],['Reports','/reports',AssessmentRounded],['Administration','/admin',AdminPanelSettingsRounded],['Settings','/settings',SettingsRounded]] as const;
const customerItems=[['Customer List','/customers',GroupsRounded],['Onboard Customer','/customers/new',PersonAddRounded],['Product & Pricing Configuration','/customer-configuration',TuneRounded]] as const;
const pricingItems=[['Pricing Overview','/pricing',PriceChangeRounded],['Pricing Editor','/pricing-editor',TuneRounded],['Approval Queue','/approval-queue',PriceChangeRounded],['Pricing Versions','/pricing-versions',PriceChangeRounded],['Pricing Overrides','/pricing-overrides',PriceChangeRounded],['Payment Products','/products',Inventory2Rounded]] as const;

export default function AppShell(){
 const [mobileOpen,setMobileOpen]=useState(false);const nav=useNavigate();const loc=useLocation();
 const customerActive=customerPaths.some(path=>loc.pathname===path)||(loc.pathname.startsWith('/customers/')&&!loc.pathname.endsWith('/new'));
 const pricingActive=pricingPaths.some(path=>loc.pathname===path);
 const [customersOpen,setCustomersOpen]=useState(customerActive);const [pricingOpen,setPricingOpen]=useState(pricingActive);
 useEffect(()=>{if(customerActive)setCustomersOpen(true);if(pricingActive)setPricingOpen(true);},[customerActive,pricingActive]);
 const selected=(path:string)=>path==='/'?loc.pathname==='/':loc.pathname===path||(path==='/customers'&&/^\/customers\/[^/]+(?:\/edit)?$/.test(loc.pathname));
 const go=(path:string)=>{nav(path);setMobileOpen(false)};
 const itemSx={mb:.5,borderRadius:2,color:'rgba(255,255,255,.82)','&.Mui-selected':{bgcolor:'rgba(255,255,255,.14)',color:'white'},'&:hover':{bgcolor:'rgba(255,255,255,.08)'}};
 const link=([label,path,Icon]:typeof primaryItems[number]|typeof customerItems[number]|typeof pricingItems[number],nested=false)=><ListItemButton key={path} selected={selected(path)} onClick={()=>go(path)} sx={{...itemSx,pl:nested?3.5:2}}><ListItemIcon sx={{minWidth:40,color:'inherit'}}><Icon fontSize={nested?'small':'medium'}/></ListItemIcon><ListItemText primary={label} primaryTypographyProps={{fontSize:nested?14:16,fontWeight:selected(path)?700:500}}/></ListItemButton>;
 const group=(label:string,Icon:typeof GroupsRounded,open:boolean,setOpen:(value:boolean)=>void,active:boolean,items:typeof customerItems|typeof pricingItems)=><><ListItemButton onClick={()=>setOpen(!open)} selected={active} sx={itemSx}><ListItemIcon sx={{minWidth:40,color:'inherit'}}><Icon/></ListItemIcon><ListItemText primary={label} primaryTypographyProps={{fontWeight:active?800:600}}/>{open?<ExpandLessRounded/>:<ExpandMoreRounded/>}</ListItemButton><Collapse in={open} timeout="auto" unmountOnExit><List disablePadding sx={{ml:1.5,pl:1,borderLeft:'1px solid rgba(255,255,255,.16)'}}>{items.map(item=>link(item,true))}</List></Collapse></>;
 const drawer=<Box sx={{height:'100%',display:'flex',flexDirection:'column',bgcolor:'#10213F',color:'white'}}><Box sx={{p:3}}><Typography variant="h5">NimbusBill</Typography><Typography variant="caption" sx={{opacity:.7}}>Payment Billing Platform</Typography></Box><List sx={{px:1.5,overflowY:'auto'}}>{link(primaryItems[0])}{group('Customers',GroupsRounded,customersOpen,setCustomersOpen,customerActive,customerItems)}{group('Pricing',PriceChangeRounded,pricingOpen,setPricingOpen,pricingActive,pricingItems)}{primaryItems.slice(1).map(item=>link(item))}</List><Box sx={{mt:'auto',p:2}}><Box sx={{p:2,borderRadius:3,bgcolor:'rgba(255,255,255,.08)'}}><Typography fontWeight={700}>Cloud native</Typography><Typography variant="caption" sx={{opacity:.7}}>AWS-ready reference UI</Typography></Box></Box></Box>;
 return <Box sx={{display:'flex',minHeight:'100vh'}}><AppBar position="fixed" color="inherit" elevation={0} sx={{ml:{md:`${drawerWidth}px`},width:{md:`calc(100% - ${drawerWidth}px)`},borderBottom:'1px solid #E7ECF3'}}><Toolbar sx={{gap:2}}><IconButton sx={{display:{md:'none'}}} onClick={()=>setMobileOpen(true)}><MenuRounded/></IconButton><Box sx={{display:'flex',alignItems:'center',bgcolor:'#F1F5F9',px:1.5,py:.5,borderRadius:3,flex:1,maxWidth:520}}><SearchRounded color="action"/><InputBase placeholder="Search customers, invoices, accounts..." sx={{ml:1,flex:1}}/></Box><Badge badgeContent={3} color="error"><NotificationsNoneRounded/></Badge><Avatar sx={{width:36,height:36}}>KR</Avatar></Toolbar></AppBar><Box component="nav" sx={{width:{md:drawerWidth},flexShrink:{md:0}}}><Drawer variant="temporary" open={mobileOpen} onClose={()=>setMobileOpen(false)} ModalProps={{keepMounted:true}} sx={{display:{xs:'block',md:'none'},'& .MuiDrawer-paper':{width:drawerWidth}}}>{drawer}</Drawer><Drawer variant="permanent" open sx={{display:{xs:'none',md:'block'},'& .MuiDrawer-paper':{width:drawerWidth,border:0}}}>{drawer}</Drawer></Box><Box component="main" sx={{flexGrow:1,p:{xs:2,md:3.5},mt:8,width:{md:`calc(100% - ${drawerWidth}px)`}}}><Outlet/></Box></Box>;
}
