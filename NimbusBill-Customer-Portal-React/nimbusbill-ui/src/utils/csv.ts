export function parseCsv(text:string):string[][]{
 const rows:string[][]=[];let row:string[]=[];let field='';let quoted=false;
 for(let i=0;i<text.length;i++){const ch=text[i];if(quoted){if(ch==='"'&&text[i+1]==='"'){field+='"';i++;}else if(ch==='"')quoted=false;else field+=ch;}else if(ch==='"')quoted=true;else if(ch===','){row.push(field.trim());field='';}else if(ch==='\n'){row.push(field.trim());if(row.some(Boolean))rows.push(row);row=[];field='';}else if(ch!=='\r')field+=ch;}
 if(quoted)throw new Error('CSV contains an unterminated quoted field');row.push(field.trim());if(row.some(Boolean))rows.push(row);return rows;
}

export function requireBatchColumns(rows:string[][]):string[][]{
 if(!rows.length)throw new Error('CSV is empty');const expected=['clientReferenceId','productCode','transactionType','amount','currency','sourceAccount','destinationAccount'];
 const header=rows[0];for(let i=0;i<5;i++)if(header[i]!==expected[i])throw new Error(`CSV column ${i+1} must be ${expected[i]}`);
 return rows.slice(1).map((row,index)=>{if(row.length<5||!row[0]||!row[1]||!row[2]||!row[3]||!row[4])throw new Error(`CSV row ${index+2} is missing a required value`);if(!Number.isFinite(Number(row[3]))||Number(row[3])<=0)throw new Error(`CSV row ${index+2} has an invalid amount`);return row;});
}
