package AcademiaGestaoWebApi.Repository;

import AcademiaGestaoWebApi.Config.ConnectionConfig;
import AcademiaGestaoWebApi.Models.Aluno;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import DataLib.AutoMapper.*;

public class AlunoRepository extends Repository<Aluno> {

    @Override
    public List<Aluno> select(Object idObject, Connection connection) throws Exception {
        UUID id = (UUID) idObject;
        CallableStatement stmt = null;
        ResultSet data = null;
        
        List<Aluno> alunos = new ArrayList<>();
        
        try{
            
            String query = "{CALL SP_S_Avaliado(?)}";
            
            stmt = connection.prepareCall(query);

            switch (id.toString()) {
                case "00000000-0000-0000-0000-000000000000":
                    stmt.setNull(1, Types.INTEGER, null); 
                    break;    
                default:
                    stmt.setString(1, id.toString());
                    break;
            }
           
            data = stmt.executeQuery();
            
            AutoMapper<Aluno> autoMapper = new AutoMapper<Aluno>(new Aluno());

            alunos = autoMapper.map(data); 
         
            return alunos;
        }catch(Exception ex){
            ex.printStackTrace();
            throw new Exception(ex);
        }                 
    }

    public List<Aluno> selectSemAvaliacao(Connection connection) throws Exception{
        
        CallableStatement stmt = null;
        ResultSet data = null;
        
        List<Aluno> alunos = new ArrayList<>();
        
        try {
            String query = "{CALL sp_select_avaliado_sem_avaliacao()}";
            
            stmt = connection.prepareCall(query);
            
            data = stmt.executeQuery();
            
            AutoMapper<Aluno> autoMapper = new AutoMapper<Aluno>(new Aluno());
            
            alunos = autoMapper.map(data);
            return alunos;
            
        }catch (Exception ex){
            ex.printStackTrace();
            throw new Exception(ex);
        }
        
    }
    
    @Override
    public boolean insert(Aluno aluno, Connection connection) throws Exception {        
        try{
            String query = "INSERT INTO aluno "
                        +   "("
                        +       "id_aluno,"
                        +       "nome,"
                        +       "data_nascimento,"
                        +       "sexo,"
                        +       "email,"
                        +       "CPF,"
                        +       "ativo"
                        +   ")"
                        +   "VALUES (uuid(), ?, ?, ?, ?, ?, ?);";
            
            stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            //Params
            stmt.setString(1, aluno.getNome());
            stmt.setDate(2, Date.valueOf(aluno.getDataNascimento()));            
            stmt.setInt(3, aluno.getSexo().getInt());
            stmt.setString(4, aluno.getEmail());
            stmt.setString(5, aluno.getCpf());
            stmt.setBoolean(6, aluno.getAtivo());

            int rows = stmt.executeUpdate();     

            if(rows <= 0){
                return false;
            }
            
            return true;
        }catch(Exception error){
            error.printStackTrace();
            throw new Exception(error);
        }
    } 
     
    @Override
    public boolean update(Aluno aluno, Connection connection) throws Exception {        
        try{

            String query = "UPDATE aluno SET "
                                + "nome = ?,"
                                + "data_nascimento = ?,"
                                + "sexo = ?,"
                                + "email = ?,"
                                + "CPF = ?,"
                                + "ativo = ? " 
                         + " WHERE id_aluno = ?;";
            
            stmt = connection.prepareStatement(query);
            
            //Params
            stmt.setString(1, aluno.getNome());
            stmt.setDate(2, Date.valueOf(aluno.getDataNascimento()));            
            stmt.setInt(3, aluno.getSexo().getInt());
            stmt.setString(4, aluno.getEmail());
            stmt.setString(5, aluno.getCpf());
            stmt.setBoolean(6, aluno.getAtivo());
            stmt.setString(7, aluno.getId().toString());

            stmt.executeUpdate();   

            return true;
        }catch(Exception error){
            error.printStackTrace();
            throw new Exception(error);
        }
    } 
    
    @Override
    public boolean delete(Object idAluno, Connection connection) throws Exception {
        
        UUID guidID = (UUID) idAluno;
        boolean temConexao = true;
        if(connection == null){
            connection = ConnectionConfig.getConnection(false);
            temConexao = false;
        }
        
        PreparedStatement stmt = null;
        
        try{
            
            String query = "UPDATE aluno SET ativo = FALSE WHERE id_aluno = ?"; 
            
            stmt = connection.prepareStatement(query);
            stmt.setString(1, idAluno.toString());
            stmt.execute();                 
            
            return true;
        }catch(SQLException error){
            if(!temConexao){
                connection.rollback();
                ConnectionConfig.closeConnection(connection, stmt);
            }

            error.printStackTrace();
            throw new Exception(error);
        }
    }
}