package AcademiaGestaoWebApi.Calculos;

import AcademiaGestaoWebApi.Enums.SexoEnum;

public class CalculosGerais{

    public CalculosGerais(SexoEnum Sexo){        
        if(Sexo == SexoEnum.MASCULINO){
            porcentagemDeGorduraDefinida = 20;
            porcentagemDeGorduraIdeal = 5;
            return;
        }

        porcentagemDeGorduraDefinida = 24;
        porcentagemDeGorduraIdeal = 8;
    }

    private double porcentagemDeGorduraDefinida;
    private double porcentagemDeGorduraIdeal;

    // Calculos de resultado simples:
    public Double imc(double massa, double estatura){
                
        double result = 0;

        if(massa != 0 && estatura != 0){
            result = massa / (estatura * estatura);
        }
        
        return result;
    }
    
    public Double pccq(double cintura, double quadril){
        
        double result = 0;
        
        if(cintura != 0 && quadril != 0){
            result = cintura / quadril;
        }
        
        return result;
    }
    
    //Calculos de Fracionamento CC:
    public Double massaDeGordura(double massa){
        
        double result = 0;
        
        if(massa != 0 && porcentagemDeGorduraDefinida != 0){
            result = massa * porcentagemDeGorduraDefinida;
        }
        
        return result;
    }
    
    public Double massaMagra(double massa, double massaDeGordura){
        
        double result = 0;
        
        if(massa != 0 && massaDeGordura != 0){            
            result = massa - massaDeGordura;
        }
        
        return result;
    }
    
    public Double pesoIdeal(double massaMagra){
        
        double result = 0;
        
        if(massaMagra != 0 && porcentagemDeGorduraIdeal != 0){
            result = massaMagra / (1 - porcentagemDeGorduraIdeal);
        }
        
        return result;
    }
    
    public Double pesoExcesso(double massa, double pesoIdeal){
        
        double result = 0;
        
        if(massa != 0 && pesoIdeal != 0){
            result = massa - pesoIdeal;
        }
        
        return result;
    }
}