-- Table Account of Bank A
CREATE TABLE ACCOUNT_BANK_A (
  id INT AUTO_INCREMENT PRIMARY KEY,
  cif_number BIGINT,
  amount BIGINT,
  type varchar(45),
  status varchar(45)
);
-- Table Account of Bank B
CREATE TABLE ACCOUNT_BANK_B (
  id INT AUTO_INCREMENT PRIMARY KEY,
  cif_number BIGINT,
  amount BIGINT,
  type varchar(45),
  status varchar(45)
);