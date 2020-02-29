package petclinic.authz

default allow = false

allow {
  input.method = "GET"
  input.path = ["pets", name]
  allowed[pet]
  pet.name = name
}

allowed[pet] {
  pet = data.pets[_]
  pet.owner = input.subject.user
}

allowed[pet] {
  pet = data.pets[_]
  pet.veterinarian = input.subject.user
  pet.clinic = input.subject.location
}