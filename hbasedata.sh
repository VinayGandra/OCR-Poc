if [ ! $# -eq 1 ]
then
 echo "Invalid or no hdfs input folder path provided"
 echo "Takes one argument - hdfs input folder path without ending slash/"
 exit 0
fi
if [ -e insertdata.txt ]
then
 rm insertdata.txt
fi
inputfolder=$1
for file in `hdfs dfs -stat "%n" ${inputfolder}/*`;
do
  echo "put 'meddb:claimdata','${file}','Info:CaseID','${file}'";
  echo "put 'meddb:claimdata','${file}','Info:Year','2012'";
  echo "put 'meddb:claimdata','${file}','Info:Month','Jan'";
  echo "put 'meddb:claimdata','${file}','Data:ImageData','ImageData'";
  echo "put 'meddb:claimdata','${file}','Data:TextData','TextData'";
done >> insertdata.txt
echo "exit" >> insertdata.txt
hbase shell insertdata.txt
rm insertdata.txt