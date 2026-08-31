import {Avatar,Box,Button,Card,CardContent,Chip,Divider,LinearProgress,Stack,Typography} from '@mui/material';
import Grid from '@mui/material/Grid2';
import AddRounded from '@mui/icons-material/AddRounded';
import GroupsRounded from '@mui/icons-material/GroupsRounded';
import PaymentsRounded from '@mui/icons-material/PaymentsRounded';
import ReceiptLongRounded from '@mui/icons-material/ReceiptLongRounded';
import TrendingUpRounded from '@mui/icons-material/TrendingUpRounded';
import {Area,AreaChart,CartesianGrid,ResponsiveContainer,Tooltip,XAxis,YAxis} from 'recharts';
import {billingSeries} from '../data/mock';

const kpis=[
 ['Total customers','1,250','+4.8%','vs last month',GroupsRounded,'#3157D5','#EEF2FF'],
 ['Active customers','1,180','94.4%','activation rate',TrendingUpRounded,'#16805D','#EAF8F2'],
 ['Monthly revenue','₹12.5 Cr','+8.2%','vs last month',PaymentsRounded,'#8B5CF6','#F3EEFF'],
 ['Pending invoices','220','₹1.8 Cr','awaiting settlement',ReceiptLongRounded,'#C66A00','#FFF4E5']
] as const;
const activity=[
 ['A','ABC Payments pricing plan approved','Pricing','12 min ago'],
 ['N','NorthStar Retail submitted onboarding','Customer','48 min ago'],
 ['M','Metro Logistics billing run completed','Billing','2 hr ago'],
 ['Z','Zenith Global billing contact updated','Customer','4 hr ago']
];

export default function Dashboard(){return <Stack spacing={3}>
 <Box display="flex" justifyContent="space-between" alignItems={{xs:'flex-start',sm:'center'}} gap={2} flexDirection={{xs:'column',sm:'row'}}>
  <Box><Stack direction="row" spacing={1} alignItems="center"><Typography variant="h4">Good evening, Karthik</Typography><Chip label="Live" size="small" color="success" variant="outlined"/></Stack><Typography color="text.secondary" mt={.5}>Your revenue operations overview for today.</Typography></Box>
  <Button variant="contained" startIcon={<AddRounded/>}>Onboard customer</Button>
 </Box>
 <Grid container spacing={2}>{kpis.map(([label,value,change,note,Icon,color,tint])=><Grid size={{xs:12,sm:6,xl:3}} key={label}><Card sx={{height:'100%',transition:'transform .2s, box-shadow .2s','&:hover':{transform:'translateY(-3px)',boxShadow:'0 12px 28px rgba(16,24,40,.09)'}}}><CardContent><Stack direction="row" justifyContent="space-between"><Box><Typography color="text.secondary" fontSize={14} fontWeight={700}>{label}</Typography><Typography variant="h4" my={1.25}>{value}</Typography><Typography fontSize={13} color={color} fontWeight={800}>{change} <Box component="span" color="text.secondary" fontWeight={500}>{note}</Box></Typography></Box><Box sx={{width:44,height:44,borderRadius:3,bgcolor:tint,color,display:'grid',placeItems:'center'}}><Icon/></Box></Stack></CardContent></Card></Grid>)}</Grid>
 <Grid container spacing={2}><Grid size={{xs:12,lg:8}}><Card><CardContent><Stack direction="row" justifyContent="space-between" mb={2}><Box><Typography variant="h6">Revenue performance</Typography><Typography color="text.secondary" fontSize={14}>Monthly billings in ₹ crore</Typography></Box><Chip label="Last 12 months" variant="outlined"/></Stack><Box height={300}><ResponsiveContainer width="100%" height="100%"><AreaChart data={billingSeries} margin={{left:-15}}><defs><linearGradient id="billingFill" x1="0" y1="0" x2="0" y2="1"><stop offset="5%" stopColor="#3157D5" stopOpacity={.24}/><stop offset="95%" stopColor="#3157D5" stopOpacity={.01}/></linearGradient></defs><CartesianGrid stroke="#EDF0F5" vertical={false}/><XAxis dataKey="month" axisLine={false} tickLine={false}/><YAxis axisLine={false} tickLine={false}/><Tooltip/><Area type="monotone" dataKey="value" stroke="#3157D5" strokeWidth={3} fill="url(#billingFill)"/></AreaChart></ResponsiveContainer></Box></CardContent></Card></Grid>
 <Grid size={{xs:12,lg:4}}><Card sx={{height:'100%'}}><CardContent><Typography variant="h6">Onboarding pipeline</Typography><Typography color="text.secondary" fontSize={14} mb={2.5}>18 customers in progress</Typography>{[['KYC review',8,70],['Pricing approval',5,46],['Account setup',3,30],['Ready to activate',2,20]].map(([name,count,value])=><Box mb={2.4} key={name as string}><Box display="flex" justifyContent="space-between" mb={.8}><Typography fontSize={14} fontWeight={650}>{name}</Typography><Typography fontWeight={800}>{count}</Typography></Box><LinearProgress variant="determinate" value={value as number} sx={{height:7,borderRadius:4,bgcolor:'#EEF1F5'}}/></Box>)}</CardContent></Card></Grid></Grid>
 <Card><CardContent><Typography variant="h6">Recent activity</Typography><Typography color="text.secondary" fontSize={14} mb={1}>Latest changes across your workspace</Typography>{activity.map(([initial,title,kind,time],i)=><Box key={title}><Stack direction="row" alignItems="center" spacing={1.5} py={1.4}><Avatar sx={{width:36,height:36,bgcolor:'#EEF2FF',color:'#3157D5',fontWeight:800,fontSize:14}}>{initial}</Avatar><Box flex={1}><Typography fontWeight={700} fontSize={14}>{title}</Typography><Typography variant="caption" color="text.secondary">{kind}</Typography></Box><Typography variant="caption" color="text.secondary">{time}</Typography></Stack>{i<activity.length-1&&<Divider/>}</Box>)}</CardContent></Card>
 </Stack>}
