SELECT DISTINCT WEBUserID
  FROM
       `marketingenrico`.`contact` JOIN `marketingenrico`.`prospectbasedb` 
       ON `marketingenrico`.`contact`.PROSPECTINTID = `marketingenrico`.`prospectbasedb`.INTERNALID
